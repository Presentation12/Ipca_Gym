namespace LayerBOL.Models
{
    public class Ginasio
    {
        /// <summary>
        /// Id do ginásio
        /// </summary>
        /// <example>1</example>
        public int id_ginasio { get; set; }
        /// <summary>
        /// Nome da instituição/estabelecimento
        /// </summary>
        /// <example>UMinho</example>
        public string instituicao { get; set; }
        /// <summary>
        /// Estado do ginásio (Ativo ou Inativo)
        /// </summary>
        /// <example>Ativo</example>
        public string estado { get; set; }
        /// <summary>
        /// Foto do ginásio que poderá ser nula
        /// </summary>
        /// <example>C:\OneDrive\ginasio.png</example>
        public string? foto_ginasio { get; set; }
        /// <summary>
        /// Contacto do ginásio
        /// </summary>
        /// <example>911922933</example>
        public int contacto { get; set; }

        /// <summary>
        /// Numero de pessoas a utilizar o ginasio
        /// </summary>
        /// <example>12</example>
        public int lotacao { get; set; }
        /// <summary>
        /// Numero de pessoas maximo que pode utilizar o ginasio
        /// </summary>
        /// <example>50</example>
        public int lotacaoMax { get; set; }
    }
}
