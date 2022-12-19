namespace Backend_IPCA_Gym.Models
{
    public class Loja
    {
        public int id_produto { get; set; }
        public int id_ginasio { get; set; }
        public string nome { get; set; }
        public string tipo_produto { get; set; }
        public double preco { get; set; }
        public string descricao { get; set; }
        public string estado { get; set; }
        public string? foto_produto { get; set; }
        public int quantidade { get; set; }
    }
}
