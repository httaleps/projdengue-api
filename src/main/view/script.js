const BASE_URL = 'http://localhost:8080'; 
let tokenJwt = localStorage.getItem('jwt') || '';

// Elementos
const formLogin = document.getElementById('formLogin');
const tokenPreview = document.getElementById('tokenPreview');
const msgLogin = document.getElementById('msgLogin');
const btnListar = document.getElementById('btnListar');
const msgCors = document.getElementById('msgCors');
const listaUsuarios = document.getElementById('listaUsuarios');

// Mostra token salvo se existir
if(tokenJwt) tokenPreview.innerText = tokenJwt.substring(0, 20) + '...';

// --- LOGIN ---
formLogin.addEventListener('submit', async (e) => {
    e.preventDefault();

    const loginInput = document.getElementById('inputLogin');
    const senhaInput = document.getElementById('inputSenha');

    // Validação extra pra não dar erro de null
    if (!loginInput || !senhaInput) {
        console.error("ERRO CRÍTICO: Não achei os inputs no HTML. Verifique os IDs.");
        return;
    }

    const corpo = {
        usuario: loginInput.value, // Verifique se seu Java espera 'login' ou 'username'
        senha: senhaInput.value
    };

    try {
        const response = await fetch(`${BASE_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(corpo)
        });

        if (response.ok) {
            // Tenta pegar o token do Header (padrão Spring Security)
            let token = response.headers.get('Authorization');
            
            // Se não vier no Header, tenta pegar do JSON
            if(!token) {
                try {
                    const json = await response.json();
                    token = json.token || json.accessToken;
                } catch(err) { console.log("Sem JSON na resposta"); }
            }

            if (token) {
                // Remove 'Bearer ' se vier duplicado
                tokenJwt = token.replace('Bearer ', '');
                localStorage.setItem('jwt', tokenJwt);
                
                msgLogin.innerText = "Login OK!";
                msgLogin.className = "status-box success";
                msgLogin.style.display = "block";
                tokenPreview.innerText = tokenJwt.substring(0, 20) + '...';
            }
        } else {
            msgLogin.innerText = "Login Falhou: " + response.status;
            msgLogin.className = "status-box error";
            msgLogin.style.display = "block";
        }
    } catch (erro) {
        msgLogin.innerText = "Erro: " + erro.message;
        msgLogin.className = "status-box error";
        msgLogin.style.display = "block";
    }
});

// --- LISTAGEM ---
btnListar.addEventListener('click', async () => {
    listaUsuarios.innerHTML = '';
    
    if(!tokenJwt) return alert("Faça login antes!");

    try {
        const response = await fetch(`${BASE_URL}/usuario`, {
            headers: { 'Authorization': `Bearer ${tokenJwt}` }
        });

        if(response.ok) {
            const lista = await response.json();
            msgCors.innerText = "Sucesso! Dados recebidos.";
            msgCors.className = "status-box success";
            msgCors.style.display = "block";

            lista.forEach(u => {
                listaUsuarios.innerHTML += `<li class="list-group-item">${u.id} - ${u.usuario}</li>`;
            });
        } else {
            throw new Error(response.status);
        }
    } catch (e) {
        msgCors.innerText = "Erro: " + e.message;
        msgCors.className = "status-box error";
        msgCors.style.display = "block";
    }
});