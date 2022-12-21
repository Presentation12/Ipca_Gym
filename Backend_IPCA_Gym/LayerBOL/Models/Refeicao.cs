namespace LayerBOL.Models
{
    public class Refeicao
    {
        /// <summary>
        /// Id de refeicao
        /// </summary>
        /// <example>1</example>
        public int id_refeicao { get; set; }
        /// <summary>
        /// Id do plano nutricional ao qual a refeicao pertence
        /// </summary>
        /// <example>1</example>
        public int id_plano_nutricional { get; set; }
        /// <summary>
        /// Descricao do plano nutricional
        /// </summary>
        /// <example>1 Banana</example>
        public string descricao { get; set; }
        /// <summary>
        /// Hora para consumir a refeicao
        /// </summary>
        /// <example>12:30:00</example>
        public TimeSpan hora { get; set; }
        /// <summary>
        /// Foto da refeicao que poderá ser nulo
        /// </summary>
        /// <example>C:\OneDrive\banana.png</example>
        public string? foto_refeicao { get; set; }
    }
}
