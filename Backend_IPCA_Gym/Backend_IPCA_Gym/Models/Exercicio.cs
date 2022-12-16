namespace Backend_IPCA_Gym.Models
{
    public class Exercicio
    {
        public int id_exercicio { get; set; }
        public int id_plano_treino { get; set; }
        public string nome { get; set; }
        public string descricao { get; set; }
        public string tipo { get; set; }
        public int? series { get; set; }
        public TimeSpan? tempo { get; set; }
        public int? repeticoes { get; set; }
    }
}
