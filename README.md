# 🔐 Gerenciador de Senhas

Um projeto em Java para armazenar e gerenciar senhas de forma segura. Desenvolvido como parte de um desafio acadêmico, o sistema inclui criptografia AES, autenticação 2FA, geração de senhas fortes e verificação de vazamentos usando uma API externa.

# 🚀 Funcionalidades

✅ Armazenar senhas criptografadas  
✅ Autenticação com dois fatores (2FA)  
✅ Geração de senhas fortes e aleatórias  
✅ Verificação de senhas vazadas (via API HaveIBeenPwned)  
✅ Listar, buscar e remover credenciais  
✅ Persistência em arquivo local (`credenciais.txt`)  

---

# 🛠️ Tecnologias Utilizadas

- Java 17+
- IntelliJ IDEA (ou outro IDE compatível)
- API: HaveIBeenPwned (para verificar vazamentos)
- Algoritmo AES (criptografia simétrica)

---

# 📦 Estrutura do Projeto
src/
├── Main.java
├── model/
│ └── Credencial.java
├── service/
│ └── GerenciadorDeSenhas.java
├── util/
│ ├── CriptografiaAES.java
│ ├── GeradorDeSenhas.java
│ └── VerificadorDeVazamento.java
├── credenciais.txt

# 🧪 Requisitos Atendidos
 Cadastro de senhas

 Criptografia AES

 Autenticação 2FA

 Geração de senhas seguras

 Verificação de vazamentos de senha via API

 Armazenamento local de credenciais

 Projeto documentado no GitHub com README

# 🔐 Segurança
As senhas são criptografadas antes de serem salvas.
A verificação de vazamento usa a técnica de k-Anonimity da API haveibeenpwned.com.
