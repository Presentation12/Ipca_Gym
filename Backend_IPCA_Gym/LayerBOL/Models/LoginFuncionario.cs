namespace LayerBOL.Models
{
    public class LoginFuncionario
    {
        /// <summary>
        /// Codigo de identificacao do funcionario
        /// </summary>
        /// <example>12543587</example>
        public int codigo { get; set; }
        /// <summary>
        /// Password do funcionario
        /// </summary>
        /// <example>1234abcd</example>
        public string password { get; set; }
    }
}
