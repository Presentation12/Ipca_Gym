namespace Backend_IPCA_Gym.Models
{
    public class Funcionario
    {
        public int id_funcionario { get; set; }
        public int id_ginasio { get; set; }
        public string nome { get; set; }
        public bool is_admin { get; set; }
        public int codigo { get; set; }
        public string pass_salt { get; set; }
        public string pass_hash { get; set; }
        public string estado { get; set; }
    }
}
