namespace LayerBOL.Models
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
