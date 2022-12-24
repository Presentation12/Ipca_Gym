using LayerBOL.Models;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace LayerDAL
{
    public class Token
    {
        /// <summary>
        /// Cria tokens de autenticação
        /// </summary>
        /// <param name="cliente"> Cliente </param>
        /// <returns> JSON web token </returns>
        public static string CreateTokenCliente(Cliente cliente, IConfiguration _configuration)
        {
            List<Claim> claims = new List<Claim>
            {
                new Claim(ClaimTypes.Email, cliente.mail),
                new Claim(ClaimTypes.Role, "Cliente")
            };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration.GetSection("AppSettings:Token").Value));

            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha512Signature);

            var token = new JwtSecurityToken(
                claims: claims,
                expires: DateTime.Now.AddDays(1),
                signingCredentials: creds);

            var jwt = new JwtSecurityTokenHandler().WriteToken(token);

            return jwt;
        }

        /// <summary>
        /// Método auxiliar que faz a criação de um Token de Sessao
        /// </summary>
        /// <param name="funcionario">Funcionario que esta a efetuar login</param>
        /// <param name="_configuration">Dependency Injection</param>
        /// <returns>Token jwt de sessao</returns>
        public static string CreateTokenFuncionario(Funcionario funcionario, IConfiguration _configuration)
        {
            string role = string.Empty;

            if (funcionario.is_admin) role = "Gerente";
            else role = "Funcionario";

            List<Claim> claims = new List<Claim>
            {
                new Claim(ClaimTypes.Email, funcionario.codigo.ToString()),
                new Claim(ClaimTypes.Role, role)
            };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration.GetSection("AppSettings:Token").Value));

            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha512Signature);

            var token = new JwtSecurityToken(
                claims: claims,
                expires: DateTime.Now.AddDays(1),
                signingCredentials: creds);

            var jwt = new JwtSecurityTokenHandler().WriteToken(token);

            return jwt;
        }
    }
}
