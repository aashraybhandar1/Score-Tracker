package consumer

import (
	"context"
	"fmt"
	"score-tracker/notification-service/kafkaInit"
)

func ConsumeMessages() {
	conn := kafkaInit.ConnectToKafka()

	fmt.Println("Starting Kafka consumer...")
	for {
		fmt.Println("In Consume message")
		msg, err := conn.ReadMessage(1e3)
		if err != nil {
			if err == context.Canceled {
				// Context was canceled, so we are gracefully shutting down.
				fmt.Println("Consumer stopped.")
				break
			}
			fmt.Printf("Error while reading message: %v\n", err)
			continue
		}
		fmt.Printf("Received message: Key=%s, Value=%s\n", string(msg.Key), string(msg.Value))
	}
}
