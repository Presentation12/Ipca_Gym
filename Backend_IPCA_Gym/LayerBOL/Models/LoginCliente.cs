using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LayerBOL.Models
{
    public class LoginCliente
    {
        /// <summary>
        /// Mail do Utilizador
        /// </summary>
        /// <example>user@mail.com</example>
        public string mail { get; set; }
        /// <summary>
        /// Password do utilizador
        /// </summary>
        /// <example>1234abcd</example>
        public string password { get; set; }
    }
}
