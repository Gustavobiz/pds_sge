import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { InputText } from 'primereact/inputtext';
import { Password } from 'primereact/password';
import { Button } from 'primereact/button';
import { Toast } from 'primereact/toast';
import '../assets/css/CadastroUsuario.css';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

const CadastroUsuario = () => {
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('');
  const toast = useRef(null);
  const navigate = useNavigate();

  let domain = 'http://localhost';
  let port = 8080;

  const showToast = (severity, summary, detail) => {
    toast.current.show({ severity, summary, detail });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const dadosPessoais = {
      nome,
      email,
    }

    try {
      const response = await fetch(`${domain}:${port}/api/pessoas/criar`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(dadosPessoais)
      });
       if (response.ok) {
         const novoPessoa = await response.json();
         const novoPessoaId = novoPessoa.id;
         const response2 = await fetch('api/auth/register', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: {
              "user": {"login": email, "password": password},
              "idDadosPessoais": novoPessoaId,
              "role": role
            }
        });
        if (response2.ok) {
          showToast('success', 'Enviado', 'Dados enviados com sucesso!');
          setNome('');
          setEmail('');
          setPassword('');
          setRole('');
        } else {
          const errorData = await response.json();
          showToast('error', 'Erro', errorData.message);
        }
      }else {
        const errorData = await response.json();
        showToast('error', 'Erro', errorData.message);
      }
    } catch (error) {
      showToast('error', 'Erro', 'Ocorreu um erro ao enviar os dados.');
      console.error('Error submitting form:', error);
    }
  };

  return (
      <div className="form-container">
        <Toast ref={toast} />
        <h1>Cadastro</h1>
        <form onSubmit={handleSubmit}>
          <div className="p-field">
            <label htmlFor="nome">Nome</label>
            <InputText id="nome" value={nome} onChange={(e) => setNome(e.target.value)} />
          </div>
          <div className="p-field">
            <label htmlFor="email">Email</label>
            <InputText id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
          </div>
          <div className="p-field">
            <label htmlFor="password">Senha</label>
            <Password id="password" value={password} onChange={(e) => setPassword(e.target.value)} toggleMask />
          </div>
          <div className="p-field">
            <label>Você é:</label>
            <div>
              <label htmlFor="discente" className="p-radiobutton-label">
                <input type="radio" id="discente" name="role" value="Discente" onChange={(e) => setRole(e.target.value)}
                       checked={role === 'Discente'}/>
                <span className="p-radiobutton-icon"></span>
                Discente
              </label>
              <label htmlFor="docente" className="p-radiobutton-label">
                <input type="radio" id="docente" name="role" value="Docente" onChange={(e) => setRole(e.target.value)}
                       checked={role === 'Docente'}/>
                <span className="p-radiobutton-icon"></span>
                Docente
              </label>
            </div>
          </div>
          <div className="p-field">
            <Button type="submit" label="Cadastrar" className="p-button-primary"/>
          </div>
        </form>
        <div className="p-field">
          <Button label="Voltar para o Login" className="p-button-secondary" onClick={() => navigate('/')}/>
        </div>
      </div>
  );
};

export default CadastroUsuario;
