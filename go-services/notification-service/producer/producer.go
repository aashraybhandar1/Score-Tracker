package producer

import (
	"fmt"
	"score-tracker/notification-service/kafkaInit"
	"strconv"
	"time"

	"github.com/segmentio/kafka-go"
)

func ProduceMessages() {
	conn := kafkaInit.ConnectToKafka()
	count := 0
	for {
		fmt.Println("In Produce message")
		count++
		time.Sleep(5 * time.Second)
		temp := "Hello Kafka " + strconv.Itoa(count)
		fmt.Println(temp)
		_, err := conn.WriteMessages(kafka.Message{Value: []byte(temp)})

		if err != nil {
			fmt.Println("Error is " + err.Error())
		}
	}
}
