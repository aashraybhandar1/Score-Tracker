package caas

type Cache interface {
	Set(key string, value []byte)
	Get(key string) []byte
}
