# CPU & Performance

Informações e dados extraídos pelo tempo de uso de 4 min

No consumo de CPU, nunca passou de 50%, tendo valores médios entre 20% e 45%. O uso da CPU foi destacado enquanto o usuário interagia diretamente no app, principalmente quando deslizaba o botão no mapa.

Segue alguns dados e análises:

- Foram utilizadas uma média de 61 threads, sendo uma delas o próprio fluxo do app.
- O uso de CPU da thread do app foi menor que de outras threads.
- O uso da CPU se destaca quando o usuário interagia com a interface do app, interagia na movimentação do mapa para regiões mais distantes da atual localização.
- O inicio de cada Atividade foi onde a CPU foi mais utilizado, no Create e Start. Como esperado, pois eram onde grande das inicializações e chamadas eram feitas.
- Em atividades mais simples, como na criação do evento (um formulário), o uso de cpu caiu bastante, só possuindo elevação dos valores na interação ou quando popups de Hora e Data apareciam.
