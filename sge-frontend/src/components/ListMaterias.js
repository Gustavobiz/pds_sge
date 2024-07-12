import React, { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Toast } from 'primereact/toast';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import '../assets/css/ListaAlunos.css';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

const ListMaterias = () => {
    const [materias, setMaterias] = useState([]);
    const [matricula, setMatricula] = useState('');
    const [loading, setLoading] = useState(true);
    const { id } = useParams();
    const toast = useRef(null);
    const domain = 'http://localhost';
    const port = 8080;

    const showToast = (severity, summary, detail) => {
        toast.current.show({ severity, summary, detail });
    };

    const fetchMatricula = async () => {
        try {
            console.log(id)
            const matriculaResponse = await fetch(`${domain}:${port}/api/matricula/${id}`);
            console.log(matriculaResponse)
            if (!matriculaResponse.ok) {
                throw new Error('Failed to fetch matricula data');
            }
            const matriculaData = await matriculaResponse.json();
            console.log(matriculaData)
            console.log(matriculaData.matricula)
            setMatricula(matriculaData.matricula);
        } catch (error) {
            console.error('Error fetching data:', error);
            showToast('error', 'Error', 'Não foi possível obter os dados do usuário.');
        } finally {
            setLoading(false);
        }
    };

    const fetchPresencas = async (alunoId) => {
        try {
            const response = await fetch(`${domain}:${port}/api/discente-materia/frequencia/${alunoId}`);
            if (!response.ok) {
                throw new Error('Failed to fetch presenca data');
            }
            const presencas = await response.json();
            const totalPresencas = presencas.length;
            const presencasTrue = presencas.filter(presenca => presenca.presenca === true).length;
            const presencaPercentage = (presencasTrue / totalPresencas) * 100;
            return presencaPercentage + '%';
        } catch (error) {
            console.error("Error fetching presenca data:", error);
            showToast('error', 'Error', `Não foi possível obter a presença para o aluno com ID ${alunoId}.`);
            return 'N/A';
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            await fetchMatricula();
            try {
                console.log(matricula)
                const response = await fetch(`${domain}:${port}/api/discente-materia/discente/${matricula}`);
                console.log(response)
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                console.log(data)
                const materiasPresenca = await Promise.all(data.map(async (materia) => {
                    const presencaPercentage = await fetchPresencas(materia.id);
                    return { ...materia, presenca: presencaPercentage };
                }));
                console.log("oKAY2")
                setMaterias(materiasPresenca);
            } catch (error) {
                showToast('error', 'Error', 'Não foi possível listar as matérias.');
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [id, matricula]);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="list-alunos-container">
            <Toast ref={toast} />
            <h1>Lista de Materias</h1>
            <DataTable value={materias}>
                <Column field="materia.nome" header="MATÉRIA" />
                <Column field="materia.descricao" header="DESCRIÇÃO" />
                <Column field="unidade1" header="UNIDADE 1" />
                <Column field="unidade2" header="UNIDADE 2" />
                <Column field="unidade3" header="UNIDADE 3" />
                <Column field="provaFinal" header="FINAL" />
                <Column field="status" header="STATUS" />
                <Column field="presenca" header="FREQUÊNCIA" />
            </DataTable>
        </div>
    );
};

export default ListMaterias;