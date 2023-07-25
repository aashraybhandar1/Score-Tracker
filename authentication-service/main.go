package main

import (
	"authentication-service/controllers"
	"authentication-service/middleware"
	"fmt"
	"log"
	"net"
	"time"

	"authentication-service/initializers"

	"github.com/gin-gonic/gin"
	"github.com/hashicorp/consul/api"
)

// Runs before main the init function
func init() {
	initializers.LoadEnvVariables()
	initializers.ConnectToDB()
	initializers.SyncDatabse()
}

const (
	ttl     = time.Second * 10
	checkID = "checkHealth"
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

func (s *Service) Start() {
	/*ln, err := net.Listen("tcp", ":3000")
	if err != nil {
		log.Fatal(err)
	}*/
	s.registerService()
	go s.updateHealthCheck()
	//s.acceptLoop(ln)
}

func (s *Service) acceptLoop(ln net.Listener) {
	for {
		_, err := ln.Accept()
		if err != nil {
			log.Fatal(err)
		}
	}
}

func (s *Service) updateHealthCheck() {
	ticker := time.NewTicker(time.Second * 5)
	for {
		err := s.consulClient.Agent().UpdateTTL(checkID, "online", api.HealthPassing)
		if err != nil {
			log.Fatal(err)
		}
		<-ticker.C
	}
}

func (s *Service) registerService() {
	check := &api.AgentServiceCheck{
		DeregisterCriticalServiceAfter: ttl.String(),
		TLSSkipVerify:                  true,
		TTL:                            ttl.String(),
		CheckID:                        checkID,
	}

	register := &api.AgentServiceRegistration{
		ID:      "login_service",
		Name:    "mycluster",
		Tags:    []string{"login"},
		Address: "127.0.0.1",
		Port:    3000,
		Check:   check,
	}
	err := s.consulClient.Agent().ServiceRegister(register)
	fmt.Println("In registerr")
	if err != nil {
		log.Fatal(err)
	}
}

func main() {
	fmt.Println("yolllooo 123456")
	s := NewService()
	s.Start()

	fmt.Println("Testing 123456")

	r := gin.Default()

	r.POST("/signup", controllers.Signup)
	r.POST("/login", controllers.Login)
	r.GET("/validate", middleware.RequireAuth, controllers.Validate)
	fmt.Println("Hello worldddinggggg")

	r.Run()
	fmt.Println("Hello worlddd")
}
