namespace Backend_IPCA_Gym.Models
{
    public class Classificao
    {
        public int id_avaliacao { get; set; }
        public int id_ginasio { get; set; }
        public int id_cliente { get; set; }
        public int avaliacao { get; set; }
        public string comentario { get; set; }
        public DateTime data_avaliacao { get; set; }
    }
}
