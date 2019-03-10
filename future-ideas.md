# Playing with FlexibleOrders

## Brainstorming for cool ideas to develop on

### Push UI
Develop random order creator to simulate production enviroment. Create a dashboard with incoming orders by backend push. 

### FO in a cluster
When increasing load of incoming orders this should be visible in the dashboard.

#### Roadmap to Google Cloud
1. Dockerizing FO and GUI
	Frontend, Backend and DB as separate Docker images
2. Evaluation: 
	Can I omit k8s for Google Cloud, or is it possible to use docker-compose for MVP?
3. Creating infrastructure as code for google cloud.
	CI/CD infrastructure
	Teraform for google cloud
