namespace Backend_IPCA_Gym.Models
{
    public class Pedido
    {
        public int id_pedido { get; set; }
        public int id_cliente { get; set; }
        public DateTime data_pedido { get; set; }
        public string estado { get; set; }
    }
}
