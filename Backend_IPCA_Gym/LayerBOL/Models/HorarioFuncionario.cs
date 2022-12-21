namespace LayerBOL.Models
{
    public class HorarioFuncionario
    {
        /// <summary>
        /// Id de um dia do horário de um funcionário
        /// </summary>
        /// <example>1</example>
        public int id_funcionario_horario { get; set; }
        /// <summary>
        /// Id do funcionário que lhe será construido o horário
        /// </summary>
        /// <example>1</example>
        public int id_funcionario { get; set; }
        /// <summary>
        /// Hora de entrada naquele determinado dia
        /// </summary>
        /// <example>09:00:00</example>
        public TimeSpan hora_entrada { get; set; }
        /// <summary>
        /// Hora de saída naquele determinado dia
        /// </summary>
        /// <example>17:00:00</example>
        public TimeSpan hora_saida { get; set; }
        /// <summary>
        /// Dia da semana
        /// </summary>
        /// <example>Segunda</example>
        public string dia_semana { get; set; }
    }
}
