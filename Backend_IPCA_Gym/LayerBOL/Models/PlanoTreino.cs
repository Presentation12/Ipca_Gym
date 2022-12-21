namespace LayerBOL.Models
{
    public class PlanoTreino
    {
        /// <summary>
        /// Id do plano de treino
        /// </summary>
        /// <example>1</example>
        public int id_plano_treino { get; set; }
        /// <summary>
        /// Id do ginásio ao qual pertence o plano de treino
        /// </summary>
        /// <example>1</example>
        public int id_ginasio { get; set; }
        /// <summary>
        /// Tipo de plano de treino
        /// </summary>
        /// <example>Emagrecer</example>
        public string tipo { get; set; }
        /// <summary>
        /// Foto do plano de treino
        /// </summary>
        /// <example>C:\OneDrive\emagrecer.png</example>
        public string? foto_plano_treino { get; set; }
    }
}
