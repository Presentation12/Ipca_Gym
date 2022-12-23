namespace LayerBOL.Models
{
    public class Funcionario
    {
        /// <summary>
        /// Id do funcionario
        /// </summary>
        /// <example>1</example>
        public int id_funcionario { get; set; }
        /// <summary>
        /// Id do ginásio ao qual o funcionário pertence
        /// </summary>
        /// <example>1</example>
        public int id_ginasio { get; set; }
        /// <summary>
        /// Nome do funcionário
        /// </summary>
        /// <example>Frederico Rogério</example>
        public string nome { get; set; }
        /// <summary>
        /// Booleano para veririfcar se um funcionário é admin ou não para determinados acessos
        /// </summary>
        /// <example>True</example>
        public bool is_admin { get; set; }
        /// <summary>
        /// Código de acesso á aplicação do funcionario
        /// </summary>
        /// <example>21135</example>
        public int codigo { get; set; }
        /// <summary>
        /// Password do funcionario
        /// </summary>
        /// <example>1234abcd</example>
        public string pass_salt { get; set; }
        /// <summary>
        /// Representação criptografica da password do funcionário
        /// </summary>
        /// <example>erfgergertngiue348975y3478t438fh348fbh3</example>
        public string? pass_hash { get; set; }
        /// <summary>
        /// Estado do funcionário (Ativo ou Inativo)
        /// </summary>
        /// <example>Ativo</example>
        public string estado { get; set; }
    }
}
