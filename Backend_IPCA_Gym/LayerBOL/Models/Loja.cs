    namespace LayerBOL.Models
{
    public class Loja
    {
        /// <summary>
        /// Id do produto da loja
        /// </summary>
        /// <example>1</example>
        public int id_produto { get; set; }
        /// <summary>
        /// Id do ginásio a quem pertence o produto
        /// </summary>
        /// <example>1</example>
        public int id_ginasio { get; set; }
        /// <summary>
        /// Nome do produto
        /// </summary>
        /// <example>T-shirt Azul</example>
        public string nome { get; set; }
        /// <summary>
        /// Tipo de produto
        /// </summary>
        /// <example>Roupa</example>
        public string tipo_produto { get; set; }
        /// <summary>
        /// Preço do produto
        /// </summary>
        /// <example>1.99</example>
        public double preco { get; set; }
        /// <summary>
        /// Descrição do produto
        /// </summary>
        /// <example>M</example>
        public string descricao { get; set; }
        /// <summary>
        /// Estado do produto
        /// </summary>
        /// <example>Ativo</example>
        public string estado { get; set; }
        /// <summary>
        /// Foto do produto que pode ser nulo
        /// </summary>
        /// <example>C:\OneDrive\tshirt.png</example>
        public string? foto_produto { get; set; }
        /// <summary>
        /// Quantidade do produto
        /// </summary>
        /// <example>100</example>
        public int quantidade { get; set; }
    }
}
