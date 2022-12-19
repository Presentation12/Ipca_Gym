namespace Backend_IPCA_Gym.Models
{
    public class PlanoTreino
    {
        public int id_plano_treino { get; set; }
        public int id_ginasio { get; set; }
        public string tipo { get; set; }
        public string? foto_plano_treino { get; set; }
    }
}
