import React, { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Toast } from 'primereact/toast';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import '../assets/css/ListaAlunos.css';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

const ListMaterias = () => {
    const [materias, setMaterias] = useState([]);
    const [timers, setTimers] = useState({});
    const [activeVideo, setActiveVideo] = useState(null);
    const { id } = useParams();
    const toast = useRef(null);

    const domain = 'http://localhost';
    const port = 8080;

    const showToast = (severity, summary, detail) => {
        toast.current.show({ severity, summary, detail });
    };

    /*const handleWatchClick = (materiaId, videoLink) => {
        setTimers((prevTimers) => ({
            ...prevTimers,
            [materiaId]: { time: 0, active: true }
        }));
        setActiveVideo(videoLink);
    };*/

    // Função handleClick deve fechar o frame do vídeo e resetar o tempo caso usuario ja tenha apertado o botão de assistir
    const handleWatchClick = (materiaId, videoLink) => {
        if (timers[materiaId]?.active) {
            setTimers((prevTimers) => ({
                ...prevTimers,
                [materiaId]: { startTime: Date.now(), active: false }
            }));
            setActiveVideo(null);
            registrarFrenquencia(materiaId, timers[materiaId].time);

        } else {
            setTimers((prevTimers) => ({
                ...prevTimers,
                [materiaId]: { startTime: Date.now(), active: true }
            }));
            setActiveVideo(videoLink);
        }
    }

    const registrarFrenquencia = (materiaId, timeStamp) => {
        const data = [{
            presenca: true,
            timestamp: timeStamp
        }]
        console.log(data);
        fetch(`${domain}:${port}/api/discente-materia/frequencia/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao registrar tempo de aula.');
            }
            showToast('success', 'Sucesso', 'Frequência registrada com sucesso.');
        })
        .catch(error => {
            showToast('error', 'Erro', 'Não foi possível registrar a frequência.');
        });
    }
    

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch(`${domain}:${port}/api/discente-materia/discente/${id}`);
                console.log(response);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                const materiasFormatted = data.map((materia) => ({
                    ...materia,
                    unidade1: materia.unidade1 || '--',
                    unidade2: materia.unidade2 || '--',
                    unidade3: materia.unidade3 || '--',
                }));
                setMaterias(materiasFormatted);
            } catch (error) {
                showToast('error', 'Error', 'Não foi possível listar os alunos.');
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        const interval = setInterval(() => {
            setTimers((prevTimers) => {
                const newTimers = { ...prevTimers };
                Object.keys(newTimers).forEach((key) => {
                    if (newTimers[key].active) {
                        const elapsedTime = Math.floor((Date.now() - newTimers[key].startTime) / 1000);
                        newTimers[key].time = elapsedTime;
                    }
                });
                return newTimers;
            });
        }, 1000);
        return () => clearInterval(interval);
    }, []);
    

    const formatTime = (seconds) => {
        const h = Math.floor(seconds / 3600);
        const m = Math.floor((seconds % 3600) / 60);
        const s = seconds % 60;
    
        const formattedTime = `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
        
        return formattedTime;
    };
    

    const renderWatchButton = (rowData) => {
        const timer = timers[rowData.id];
        const btnstatus = timer?.active ? "danger" : "primary";
        const buttonText = timer?.active ? 'Assistindo Aula' : 'Assistir Aula';
        const timerText = timer?.active ? ` (${formatTime(timer.time)})` : '';
        const icon = timer?.active ? 'pi pi-hourglass pi-spin' : 'pi pi-play';

        return (
            <Button
                label={`${buttonText}${timerText}`}
                severity={`${btnstatus}`}
                onClick={() => handleWatchClick(rowData.id, 'https://www.youtube.com/embed/dQw4w9WgXcQ')}
                icon={icon}
            />
        );
    };

    return (
        <div className="list-alunos-container">
            <Toast ref={toast} />
            <h1>Lista de Aulas</h1>
            <DataTable value={materias}>
                <Column field="materia.nome" header="AULA" />
                <Column field="materia.descricao" header="DESCRIÇÃO" />
                <Column field="unidade1" header="UNIDADE 1" />
                <Column field="unidade2" header="UNIDADE 2" />
                <Column field="unidade3" header="UNIDADE 3" />
                <Column field="presenca" header="FREQUÊNCIA" />
                <Column body={renderWatchButton} header="AÇÕES" />
            </DataTable>
            {activeVideo && (
                <div className="video-container">
                    <iframe
                        width="560"
                        height="315"
                        src={activeVideo}
                        frameBorder="0"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                        allowFullScreen
                        title="Video Aula"
                    ></iframe>
                </div>
            )}
        </div>
    );
};

export default ListMaterias;
