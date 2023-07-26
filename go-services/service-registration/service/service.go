package service

import (
	"log"
	"time"

	"github.com/hashicorp/consul/api"
)

type Service struct {
	consulClient *api.Client
}

func NewService() *Service {
	client, err := api.NewClient(&api.Config{})
	if err != nil {
		log.Fatal(err)
	}
	return &Service{
		consulClient: client,
	}
}

func (s *Service) Start(register *api.AgentServiceRegistration, checkId string) {
	s.RegisterService(register)
	go s.UpdateHealthCheck(checkId)
}

func (s *Service) UpdateHealthCheck(checkID string) {
	ticker := time.NewTicker(time.Second * 5)
	for {
		err := s.consulClient.Agent().UpdateTTL(checkID, "online", api.HealthPassing)
		if err != nil {
			log.Fatal(err)
		}
		<-ticker.C
	}
}

func (s *Service) RegisterService(register *api.AgentServiceRegistration) {
	err := s.consulClient.Agent().ServiceRegister(register)
	if err != nil {
		log.Fatal(err)
	}
}
