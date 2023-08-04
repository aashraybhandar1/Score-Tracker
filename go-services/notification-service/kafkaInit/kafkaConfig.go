package kafkaInit

import (
	"context"
	"time"

	"github.com/segmentio/kafka-go"
)

func ConnectToKafka() *kafka.Conn {
	conn, err := kafka.DialLeader(context.Background(), "tcp", "localhost:9092", "quickstart-events", 0)

	if err != nil {
		panic("Failed to connect to Kafka")
	}
	conn.SetWriteDeadline(time.Now().Add(time.Second * 10))
	conn.SetWriteDeadline(time.Now().Add(time.Second * 8))

	return conn
}
