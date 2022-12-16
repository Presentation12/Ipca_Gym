namespace Backend_IPCA_Gym.Models
{
    public class HorarioFuncionario
    {
        public int id_funcionario_horario { get; set; }
        public int id_funcionario { get; set; }
        public TimeSpan hora_entrada { get; set; }
        public TimeSpan hora_saida { get; set; }
        public string dia_semana { get; set; }
    }
}
