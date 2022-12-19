namespace Backend_IPCA_Gym.Models
{
    public class Marcacao
    {
        public int id_marcacao { get; set; }
        public int id_funcionario { get; set; }
        public int id_cliente { get; set; }
        public DateTime data_marcacao { get; set; }
        public string descricao { get; set; }
        public string estado { get; set; }
    }
}
