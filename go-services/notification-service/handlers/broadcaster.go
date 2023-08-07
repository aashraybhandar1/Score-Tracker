package handlers

import (
	"fmt"
	"log"
)

type Broadcaster struct {
	newConnection   chan chan []byte
	closeConnection chan chan []byte
	connections     map[chan []byte]int32
}

func (b *Broadcaster) Listen() {
	var seq int32 = 0
	for {
		select {
		case connection := <-b.newConnection:
			b.connections[connection] = seq
			seq++
			fmt.Println("new connection")

		case connection := <-b.closeConnection:
			delete(b.connections, connection)
			log.Println("connection closed")
		}
	}
}

func NewBroadcaster() *Broadcaster {
	broker := Broadcaster{
		newConnection:   make(chan chan []byte),
		closeConnection: make(chan chan []byte),
		connections:     make(map[chan []byte]int32),
	}
	return &broker
}

type Server struct {
	broadcaster *Broadcaster
}

func NewServer() *Server {
	broadcaster := NewBroadcaster()
	server := &Server{
		broadcaster: broadcaster,
	}
	go broadcaster.Listen()
	return server
}
