import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Toast } from 'primereact/toast';
import '../assets/css/CadastroUsuario.css';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

const LoginUsuario = () => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const toast = useRef(null);
    const navigate = useNavigate();

    const domain = 'http://localhost';
    const port = 8080;


    const showToast = (severity, summary, detail) => {
        toast.current.show({ severity, summary, detail });
    };

    // Function to fetch Docente by IdDadosPessoais
    const fetchDocenteByIdPessoa = async (idPessoa) => {
        const response = await fetch(`${domain}:${port}/api/docentes/idPessoa/${idPessoa}`);
        if (!response.ok) {
            throw new Error('Failed to fetch docente by IdDadosPessoais');
        }
        const docenteData = await response.json();
        return docenteData.id; // Assuming the response contains an id field
    };

    // Function to fetch Discente by IdDadosPessoais
    const fetchDiscenteByIdPessoa = async (idPessoa) => {
        const response = await fetch(`${domain}:${port}/api/discentes/idPessoa/${idPessoa}`);
        if (!response.ok) {
            throw new Error('Failed to fetch discente by IdDadosPessoais');
        }
        const discenteData = await response.json();
        return discenteData.id; // Assuming the response contains an id field
    };
    const handleSubmit = async (e) => {
        e.preventDefault();

        const loginData = {
            user: {
                "login": login,
                "senha": password
            }
        };

        try {
            const response = await fetch(`${domain}:${port}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData)
            });

            if (response.ok) {
                const userData = await response.json();
                showToast('success', 'Success', 'Usuário logado com sucesso!');

                // Check user role and fetch respective ID
                if (userData.role === "DOCENTE") {
                    const docenteId = await fetchDocenteByIdPessoa(userData.idDadosPessoais);
                    navigate(`/home-docente/${userData.idDadosPessoais}/${docenteId}`); // Navigate using docenteId
                } else if (userData.role === "DISCENTE") {
                    const discenteId = await fetchDiscenteByIdPessoa(userData.idDadosPessoais);
                    navigate(`/home-discente/${userData.idDadosPessoais}/${discenteId}`); // Navigate using discenteId
                }

            } else {
                showToast('error', 'Error', 'Ocorreu um erro ao fazer seu login.');
            }
        } catch (error) {
            showToast('error', 'Error', 'Ocorreu um erro ao fazer seu login.');
            console.error('Error logging in:', error);
        }
    };
    return (
        <div className="form-container">
            <Toast ref={toast} />
            <h1>Login</h1>
            <form onSubmit={handleSubmit}>
                <div className="p-field">
                    <label htmlFor="login">Usuário</label>
                    <InputText id="login" value={login} onChange={(e) => setLogin(e.target.value)} />
                </div>
                <div className="p-field">
                    <label htmlFor="password">Senha</label>
                    <InputText id="password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                <div className="p-field">
                    <Button type="submit" label="Login" className="p-button-primary" />
                </div>
            </form>
            <div className="p-field">
                <Button label="Ir para Cadastro" className="p-button-secondary" onClick={() => navigate('/cadastro-usuario')} />
            </div>
        </div>
    );
};

export default LoginUsuario;
