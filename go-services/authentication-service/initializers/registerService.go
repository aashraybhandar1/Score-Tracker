package initializers

import (
	"os"
	"score-tracker/service-registration/service"
	"strconv"
	"time"

	"github.com/hashicorp/consul/api"
)

const (
	ttl     = time.Second * 10
	checkID = "checkHealth"
)

func RegisterService() {

	check := &api.AgentServiceCheck{
		DeregisterCriticalServiceAfter: ttl.String(),
		TLSSkipVerify:                  true,
		TTL:                            ttl.String(),
		CheckID:                        checkID,
	}

	port, _ := strconv.Atoi(os.Getenv("PORT"))

	register := &api.AgentServiceRegistration{
		ID:      os.Getenv("SERVICE_ID"),
		Name:    os.Getenv("SERVICE_NAME"),
		Tags:    []string{"login"},
		Address: os.Getenv("HOST"),
		Port:    port,
		Check:   check,
	}

	s := service.NewService()
	s.Start(register, checkID)

}
