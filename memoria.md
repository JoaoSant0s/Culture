# Memória

Informações e dados extraídos pelo tempo de uso de 4 min

O consumo de memória nunca passou os 384MB. Como esperado, a inicialização do app usou grande parte da memória, para definir o ambiente nativo e após isso, estabilizou num mínimo de 128MB, subindo poucos número na medida do uso do app.

Segue alguns dados e análises:

- O gráfico foi o que mais consumiu memória, principalmente por se tratar de um app de mapa.
- O segundo que mais usou foi o nativo, visto que o código era mais limpo, pela facilidade de uso do Kotlin e chamadas específicas.
- Pelo tempo de execução baixo do app, não foi encontrado um Garbage Collector na atividade.
- O consumo gráfico pode ser observado pelo carregamento do mapa, a medida que o usuário se movimenta.
- Percebemos que o algoritmo de alocação de memória funcionou bem, pois, após ver que o uso de memória aumentou, para a quantidade de memória prevista, ele começou a planejar uso de memórias maiores, para evitar problemas de desempenho em alocações desnecessárias.


