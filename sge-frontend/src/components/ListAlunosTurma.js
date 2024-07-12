import React, { useState, useRef, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Toast } from 'primereact/toast';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputNumber } from 'primereact/inputnumber';
import { Button } from 'primereact/button';
import '../assets/css/ListaAlunos.css';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

const ListAlunosTurma = () => {
    const [alunos, setAlunos] = useState([]);
    const [editingRows, setEditingRows] = useState({});
    const { id } = useParams();
    const toast = useRef(null);
    const navigate = useNavigate();

    const domain = 'http://localhost';
    const port = 8080;
    const port_react = 3000;

    const showToast = (severity, summary, detail) => {
        toast.current.show({ severity, summary, detail });
    };

    useEffect(() => {

        const fetchPresencas = async (alunoId) => {
            try {
                const response = await fetch(`${domain}:${port}/api/discente-materia/frequencia/${alunoId}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch presenca data');
                }
                const presencas = await response.json();
                console.log(presencas);
                const totalPresencas = presencas.length;
                const presencasTrue = presencas.filter(presenca => presenca.presenca === true).length;
                const presencaPercentage = (presencasTrue / totalPresencas) * 100;
                return presencaPercentage;
            } catch (error) {
                console.error("Error fetching presenca data:", error);
                showToast('error', 'Error', `Não foi possível obter a presença para o aluno com ID ${alunoId}.`);
                return 'N/A';
            }
        };

        const fetchMedia = async (id) => {
            try {
                const response = await fetch(`${domain}:${port}/api/discente-materia/calcular-nota/${id}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch grade');
                }
                const data = await response.json();
                return data;
            } catch (error) {
                console.error("Error fetching grade:", error);
                showToast('error', 'Error', `Não foi possível obter a média para o aluno com ID ${id}.`);
                return null;
            }
        };

        const fetchDiscenteByMatricula = async (matricula) => {
            try {
                const response = await fetch(`${domain}:${port}/api/discentes/matricula/${matricula}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch discente data');
                }
                return await response.json();
            } catch (error) {
                console.error("Error fetching discente data:", error);
                showToast('error', 'Error', `Não foi possível obter dados do discente com matrícula ${matricula}.`);
                return null;
            }
        };
        const fetchAlunos = async () => {
            try {
                const response = await fetch(`${domain}:${port}/api/discente-materia/materia/${id}`);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                const alunosWithMatricula = await Promise.all(data.map(async (aluno) => {
                    const matriculaResponse = await fetch(`${domain}:${port}/api/matricula/discente/${aluno.matricula_discente.matricula}`);
                    const dadosAluno = await fetchDiscenteByMatricula(aluno.matricula_discente.matricula);
                    if (!matriculaResponse.ok) {
                        throw new Error('Failed to fetch matricula data');
                    }
                    const presencaPercentage = await fetchPresencas(aluno.id);
                    const matriculaData = await matriculaResponse.json();
                    const mediaData = await fetchMedia(aluno.id);
                    return { ...aluno, matricula: matriculaData.matricula , presenca: presencaPercentage, nome: dadosAluno.dadosPessoais.nome, media: mediaData.toFixed(2)};
                }));

                setAlunos(alunosWithMatricula);
            } catch (error) {
                showToast('error', 'Error', 'Não foi possível listar os alunos.');
            }
        };
        fetchAlunos();
    }, [id]);

    const onRowEditChange = (e) => {
        setEditingRows(e.data);
    };

    const onRowEditComplete = async (e) => {
        const { newData, index } = e;
        try {
            const response = await fetch(`${domain}:${port}/api/discente-materia/notas/${newData.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    unidade1: newData.unidade1,
                    unidade2: newData.unidade2,
                    unidade3: newData.unidade3,
                    provaFinal: newData.provaFinal
                }),
            });
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const updatedAlunos = [...alunos];
            updatedAlunos[index] = newData;
            setAlunos(updatedAlunos);
            showToast('success', 'Success', 'Notas updated successfully.');
        } catch (error) {
            showToast('error', 'Error', 'Não foi possível atualizar as notas.');
        }
    };

    const inputNumberEditor = (options) => {
        return (
            <InputNumber
                value={options.value}
                onValueChange={(e) => options.editorCallback(e.value)}
                mode="decimal"
                minFractionDigits={1}
                maxFractionDigits={1}
                min={0}
                max={10}
            />
        );
    };

    const incrementPresenca = async (rowData,val) => {
        const currentTimestamp = new Date().toISOString();
        const newPresenca = currentTimestamp;
        console.log(newPresenca);
        try {
            const response = await fetch(`${domain}:${port}/api/discente-materia/frequencia/edit/${rowData.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    data: newPresenca,
                    presenca: val
                }),
            });
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const updatedPresencas = await response.json();
            const totalPresencas = updatedPresencas.length;
            const presencasTrue = updatedPresencas.filter(presenca => presenca.presenca === true).length;
            const presencaPercentage = (presencasTrue / totalPresencas) * 100;

            const updatedAlunos = alunos.map(aluno =>
                aluno.id === rowData.id ? { ...aluno, presenca: presencaPercentage } : aluno
            );
            setAlunos(updatedAlunos);
            showToast('success', 'Success', 'Presença updated successfully.');
        } catch (error) {
            showToast('error', 'Error', 'Não foi possível atualizar a presença.');
        }
    };

    const presencaTemplate = (rowData) => {
        const presencaValue = rowData?.presenca ? `${rowData.presenca.toFixed(2)}%` : 'N/A';
        return (
            <div>
                {presencaValue}
                <Button icon="pi pi-plus" className="p-button-rounded p-button-success p-ml-2" style={{ marginLeft: '10px' }} onClick={() => incrementPresenca(rowData,true)} />
                <Button icon="pi pi-minus" className="p-button-rounded p-button-success p-ml-2" style={{ marginLeft: '10px' }} onClick={() => incrementPresenca(rowData,false)} />
            </div>
        );
    };

    const relatorioTemplate = (rowData) => {
        return (
            <Button label="Relatório" icon="pi pi-file" className="p-button-rounded p-button-info" onClick={() => window.location.href = `${domain}:${port_react}/relatorio/${rowData.matricula_discente.id}`} />
        );
    };

    const observacoesButtonTemplate = (rowData) => {
        return (
            <Button label="Observações" icon="pi pi-eye" className="p-button-rounded p-button-info" onClick={() => navigate(`/observacoes/${id}/${rowData.matricula}`)} />
        );
    };
    const handleConcluirSemestre = async () => {
        try {
            await Promise.all(alunos.map(aluno =>
                fetch(`${domain}:${port}/api/matricula/discente/aprovacao/${aluno.matricula_discente.matricula}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    }
                })
            ));
            showToast('success', 'Sucesso', 'Semestre concluído para todos os discentes.');
        } catch (error) {
            console.error("Error concluding semester for discentes:", error);
            showToast('error', 'Erro', 'Não foi possível concluir o semestre para todos os discentes.');
        }
    };

    return (
        <div className="list-alunos-container">
            <Toast ref={toast} />
            <Button label="Concluir Semestre" className="p-button-rounded p-button-success" onClick={handleConcluirSemestre} />
            <h1>Lista de Alunos</h1>
            <DataTable value={alunos} responsiveLayout="scroll" editMode="row" dataKey="id"
                       editingRows={editingRows} onRowEditChange={onRowEditChange} onRowEditComplete={onRowEditComplete}>
                <Column field="nome" header="ALUNO" />
                <Column field="unidade1" header="UNIDADE 1" editor={inputNumberEditor} />
                <Column field="unidade2" header="UNIDADE 2" editor={inputNumberEditor} />
                <Column field="unidade3" header="UNIDADE 3" editor={inputNumberEditor} />
                <Column field="media" header="MEDIA"/>
                <Column field="provaFinal" header="FINAL" editor={inputNumberEditor} />
                <Column field="status" header="STATUS" />
                <Column body={presencaTemplate} header="FREQUÊNCIA" />
                <Column body={relatorioTemplate} header="RELATÓRIO" />
                <Column body={observacoesButtonTemplate} header="OBSERVAÇÕES" />
                <Column rowEditor headerStyle={{ width: '7rem' }} bodyStyle={{ textAlign: 'center' }} />
            </DataTable>
        </div>
    );
};

export default ListAlunosTurma;