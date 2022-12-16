namespace Backend_IPCA_Gym.Models
{
    public class Cliente
    {
        public int id_cliente { get; set; }
        public int id_ginasio { get; set; }
        public int? id_plano_nutricional { get; set; }
        public string nome { get; set; }
        public string mail { get; set; }
        public int telemovel { get; set; }
        public string pass_salt { get; set; }
        public string pass_hash { get; set; }
        public double? peso { get; set; }
        public int? altura { get; set; }
        public double? gordura { get; set; }
        public string? foto_perfil { get; set; }
        public string estado { get; set; }

    }
}
