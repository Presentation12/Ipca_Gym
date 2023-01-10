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
        public static LoginModel CreateTokenCliente(Cliente cliente, IConfiguration _configuration)
        {
            var modelResult = new LoginModel();

            List<Claim> claims = new List<Claim>
            {
                new Claim(ClaimTypes.Email, cliente.mail),
                new Claim(ClaimTypes.Role, "Cliente")
            };

            modelResult.role = "Cliente";

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration.GetSection("AppSettings:Token").Value));

            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha512Signature);

            var token = new JwtSecurityToken(
                claims: claims,
                expires: DateTime.Now.AddDays(365),
                signingCredentials: creds);

            var jwt = new JwtSecurityTokenHandler().WriteToken(token);

            modelResult.token = jwt;

            return modelResult;
        }

        /// <summary>
        /// Método auxiliar que faz a criação de um Token de Sessao
        /// </summary>
        /// <param name="funcionario">Funcionario que esta a efetuar login</param>
        /// <param name="_configuration">Dependency Injection</param>
        /// <returns>Token jwt de sessao</returns>
        public static LoginModel CreateTokenFuncionario(Funcionario funcionario, IConfiguration _configuration)
        {
            string role = string.Empty;
            var modelResult = new LoginModel();

            if (funcionario.is_admin)
            {
                role = "Gerente";

                if (funcionario.nome == "adminaccount")
                    role = "Admin";
            }
            else
                role = "Funcionario";

            modelResult.role = role;

            List<Claim> claims = new List<Claim>
            {
                new Claim(ClaimTypes.Email, funcionario.codigo.ToString()),
                new Claim(ClaimTypes.Role, role),
                new Claim(ClaimTypes.SerialNumber, funcionario.id_ginasio.ToString())
            };

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration.GetSection("AppSettings:Token").Value));

            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha512Signature);

            var token = new JwtSecurityToken(
                claims: claims,
                expires: DateTime.Now.AddDays(365),
                signingCredentials: creds);

            var jwt = new JwtSecurityTokenHandler().WriteToken(token);
            modelResult.token = jwt;

            return modelResult;
        }
    }
}
