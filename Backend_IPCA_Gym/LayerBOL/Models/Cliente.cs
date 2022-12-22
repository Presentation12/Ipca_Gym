namespace LayerBOL.Models
{
    public class Cliente
    {
        /// <summary>
        /// Id do cliente
        /// </summary>
        /// <example>1</example>
        public int id_cliente { get; set; }
        /// <summary>
        /// Id do ginásio em que o cliente está inscrito
        /// </summary>
        /// <example>1</example>
        public int id_ginasio { get; set; }
        /// <summary>
        /// id do plano nutricional selecionado pelo cliente, podendo este ser nulo
        /// </summary>
        /// <example>1</example>
        public int? id_plano_nutricional { get; set; }
        /// <summary>
        /// Nome do cliente
        /// </summary>
        /// <example>Joaquim Botelho</example>
        public string nome { get; set; }
        /// <summary>
        /// Mail do cliente
        /// </summary>
        /// <example>jjj@gmail.com</example>
        public string mail { get; set; }
        /// <summary>
        /// Contacto do cliente
        /// </summary>
        /// <example>a21111@gmail.com</example>
        public int telemovel { get; set; }
        /// <summary>
        /// Password do cliente
        /// </summary>
        /// <example>aaa222</example>
        public string pass_salt { get; set; }
        /// <summary>
        /// Representação criptografica da password do cliente
        /// </summary>
        /// <example>grtbgvr5j945-tj48fv94yugb4845</example>
        public string? pass_hash { get; set; }
        /// <summary>
        /// Peso do cliente em kg sendo este inicializado nulo
        /// </summary>
        /// <example>65.7</example>
        public double? peso { get; set; }
        /// <summary>
        /// Altura do cliente em cm sendo este inicializado nulo
        /// </summary>
        /// <example>176</example>
        public int? altura { get; set; }
        /// <summary>
        /// Gordura percentual do cliente sendo este inicializado nulo
        /// </summary>
        /// <example>10.5</example>
        public double? gordura { get; set; }
        /// <summary>
        /// Foto de perfil do cliente podendo ser nulo
        /// </summary>
        /// <example>C:\OneDrive\joaquim.png</example>
        public string? foto_perfil { get; set; }
        /// <summary>
        /// Estado do cliente (Ativo ou Inativo)
        /// </summary>
        /// <example>Inativo</example>
        public string estado { get; set; }

    }
}
