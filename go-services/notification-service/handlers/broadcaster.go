package handlers

import (
	"fmt"
	"log"
	"strconv"
	"time"
)

type Broadcaster struct {
	NewConnection   chan chan string
	CloseConnection chan int32
	Connections     map[int32]chan string
}

func (b *Broadcaster) Listen() {
	var seq int32 = 0
	for {
		select {
		case connection := <-b.NewConnection:
			b.Connections[seq] = connection
			seq++
			fmt.Println("new connection")

		case connection := <-b.CloseConnection:
			delete(b.Connections, connection)
			log.Println("connection closed")
		}
	}
}

func NewBroadcaster() *Broadcaster {
	broker := Broadcaster{
		NewConnection:   make(chan chan string),
		CloseConnection: make(chan int32),
		Connections:     make(map[int32]chan string),
	}
	return &broker
}

type Server struct {
	Broadcaster *Broadcaster
}

func NewServer() *Server {
	broadcaster := NewBroadcaster()
	server := &Server{
		Broadcaster: broadcaster,
	}
	go broadcaster.Listen()
	go func() {
		for {
			update := time.Now().Format("2006-01-02 15:04:05")
			for k := range server.Broadcaster.Connections {
				server.Broadcaster.Connections[k] <- "Hello you're connection " + strconv.Itoa(int(k)) + " " + update
			}
			time.Sleep(60 * time.Second) // Send updates every 1 min
		}
	}()
	return server
}
