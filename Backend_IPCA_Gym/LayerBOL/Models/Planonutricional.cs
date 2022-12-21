namespace LayerBOL.Models
{
    public class PlanoNutricional
    {
        /// <summary>
        /// Id do plano nutricional
        /// </summary>
        /// <example>1</example>
        public int id_plano_nutricional { get; set; }
        /// <summary>
        /// Id do ginásio ao qual pertence o plano nutricional
        /// </summary>
        /// <example>1</example>
        public int id_ginasio { get; set; }
        /// <summary>
        /// Tipo de plano nutricional
        /// </summary>
        /// <example>Emagrecer</example>
        public string tipo { get; set; }
        /// <summary>
        /// Quantidade de calorias (estimativa) que o cliente terá de consumir
        /// </summary>
        /// <example>2000</example>
        public int calorias { get; set; }
        /// <summary>
        /// Foto do plano nutricional que poderá ser nulo
        /// </summary>
        /// <example>C:\OneDrive\emagrecer.png</example>
        public string? foto_plano_nutricional { get; set; }
    }
}
