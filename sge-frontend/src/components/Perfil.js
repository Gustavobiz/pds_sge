import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Panel } from 'primereact/panel';
import '../assets/css/Perfil.css';
import logo from '../assets/images/logo.svg';

const Perfil = () => {
    const [userData, setUserData] = useState(null);
    const [matricula, setMatricula] = useState('');
    const [role, setRole] = useState('');
    const [status, setStatus] = useState('');
    const [loading, setLoading] = useState(true);
    const { userId, id } = useParams();
    const domain = 'http://localhost';
    const port = 8080;

    const fetchUserData = async () => {
        const response = await fetch(`${domain}:${port}/api/pessoas/${userId}`);
        if (!response.ok) {
            throw new Error('Failed to fetch user data');
        }
        return response.json();
    };

    const fetchRoleData = async () => {
        const roleResponse = await fetch(`${domain}:${port}/auth/check-role/${userId}`);
        if (!roleResponse.ok) {
            console.error('Failed to fetch role data, status:', roleResponse.status);
            return null;
        }
        return roleResponse.json();
    };

    const fetchMatriculaData = async (role) => {
        const endpoint = role === 'DOCENTE' ? `/api/matricula_docente/${id}` : `/api/matricula/${id}`;
        const matriculaResponse = await fetch(`${domain}:${port}${endpoint}`);
        if (!matriculaResponse.ok) {
            throw new Error('Failed to fetch matricula data');
        }
        return matriculaResponse.json();
    };

    const initializeData = async () => {
        try {
            setLoading(true);
            const userData = await fetchUserData();
            setUserData(userData);

            const roleData = await fetchRoleData();
            setRole(roleData.role);

            const matriculaData = await fetchMatriculaData(roleData.role);
            console.log("mData:" + matriculaData);
            setMatricula(matriculaData.matricula);
            setStatus(matriculaData.status);
        } catch (error) {
            console.error('Error initializing data:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        initializeData();
    }, [userId, id]);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="profile-page">
            <Panel className="profile-panel" header={role} style={{ padding: '20px' }}>
                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '20px' }}>
                    <img src={logo} alt="Logo" style={{ width: '100px', height: 'auto', marginRight: '20px' }} />
                    <div>
                        <p className="profile-label">Nome: {userData?.nome}</p>
                        <p className="profile-label">Email: {userData?.email}</p>
                        <p className="profile-label">Matricula: {matricula}</p>
                        {role === 'DISCENTE' && <p className="profile-label">Status: {status}</p>}
                    </div>
                </div>
            </Panel>
        </div>
    );
};

export default Perfil;