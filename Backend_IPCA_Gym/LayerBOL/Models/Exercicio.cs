namespace LayerBOL.Models
{
    public class Exercicio
    {
        /// <summary>
        /// Id do exercicio
        /// </summary>
        /// <example>1</example>
        public int id_exercicio { get; set; }
        /// <summary>
        /// Id do plano de treino ao qual este pertence
        /// </summary>
        /// <example>1</example>
        public int id_plano_treino { get; set; }
        /// <summary>
        /// Nome do exercicio
        /// </summary>
        /// <example>Flexões</example>
        public string nome { get; set; }
        /// <summary>
        /// Descrição do exercicio
        /// </summary>
        /// <example>Reflita os braços até um anmgulo de 90 graus</example>
        public string descricao { get; set; }
        /// <summary>
        /// Tipo de exercicio
        /// </summary>
        /// <example>Bicep</example>
        public string tipo { get; set; }
        /// <summary>
        /// Series de um exercicio (Cada serie é composta por x repeticoes)
        /// </summary>
        /// <example>4</example>
        public int? series { get; set; }
        /// <summary>
        /// Tempo de um exercicio (Por exemplo em cardio)
        /// </summary>
        /// <example>00:30:00</example>
        public TimeSpan? tempo { get; set; }
        /// <summary>
        /// Repeticoes de um exercicio por cada serie
        /// </summary>
        /// <example>20</example>
        public int? repeticoes { get; set; }
        /// <summary>
        /// Foto do exercicio que pode ser nulo
        /// </summary>
        /// <example>C:\OneDrive\exercicio.png</example>
        public string? foto_exercicio { get; set; }
    }
}
