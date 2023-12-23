# Roda Sonar local

## Usando Docker

Iniciar o servidor:
```bash
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest
```
Assim que sua instância estiver instalada e funcionando, faça login em http://localhost:9000 usando credenciais de administrador do sistema
```
login: admin
password: admin
```
Apos acessa crie um projeto e gere um token local conforme esse artigo:
- https://www.linkedin.com/pulse/qualidade-do-c%C3%B3digo-com-sonarqube-e-docker-tiago-perroni/?originalSubdomain=pt

Agora basta abrir o terminal e execulta o comando fornecido no sonar como esse de exemplo:
```bash
mvn clean verify sonar:sonar   -Dsonar.projectKey=TECHFLIX   -Dsonar.projectName='TECHFLIX'   -Dsonar.host.url=http://localhost:9000   -Dsonar.token=sqp_b224e35b15ccf47bdf0d3fa9453daa2e8a876a3c
```
## Referências
- https://www.baeldung.com/sonarqube-jacoco-code-coverage
- https://www.linkedin.com/pulse/qualidade-do-c%C3%B3digo-com-sonarqube-e-docker-tiago-perroni/?originalSubdomain=pt
- https://docs.sonarsource.com/sonarqube/latest/try-out-sonarqube/
