namespace Backend_IPCA_Gym.Models
{
    public class PlanoNutricional
    {
        public int id_plano_nutricional { get; set; }
        public int id_ginasio { get; set; }
        public string tipo { get; set; }
        public int calorias { get; set; }
        public string? foto_plano_nutricional { get; set; }
    }
}
