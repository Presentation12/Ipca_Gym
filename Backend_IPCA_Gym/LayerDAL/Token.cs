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
        private readonly IConfiguration _configuration;

        public Token(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        /// <summary>
        /// Cria tokens de autenticação
        /// </summary>
        /// <param name="cliente"> Cliente </param>
        /// <returns> JSON web token </returns>
        private string CreateTokenCliente(Cliente cliente)
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
    }
}
