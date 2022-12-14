using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly IConfiguration _config;
        public AuthController(IConfiguration config)
        {
            _config = config;
        }

        [HttpPost("login")]
        public IActionResult Login(LoginModel user)
        {
            string jwtToken = string.Empty;

            if(user.UserName == user.Password)
            {
                var issuer = string.Empty;
                var audiennce = string.Empty;

                var key = Encoding.UTF8.GetBytes(_config["Jwt:Key"]);
                
                
                
                jwtToken = GerarTokenJWT();
                
                return Ok(jwtToken);
            }

            return Unauthorized();
            /*bool resultado = ValidarUser(user);
            if (resultado)
            {
                var jwtToken = GerarTokenJWT();
                return new AuthenticateResponse(user, wtToken);
            }
            else
            {
                return null;
            }*/
        }
        private string GerarTokenJWT()
        {
            var issuer = _config["Jwt:Issuer"];
            var audience = _config["Jwt:Audience"];
            var expiry = DateTime.Now.AddDays(1); //válido por 2 horas
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config["Jwt:Key"]));
            var credentials = new SigningCredentials(securityKey,SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(issuer: issuer, audience: audience,expires: expiry, signingCredentials: credentials);
            
            var tokenHandler = new JwtSecurityTokenHandler();

            var stringToken = tokenHandler.WriteToken(token);
            return stringToken;
        }

        [HttpGet("gettoken")]
        public Object GetToken()
        {
            string key = "my_secret_key_12345";
            var issuer = "http://xpto.com";
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);
            //Create a List of Claims, Keep claims name short
            var permClaims = new List<Claim>();
            permClaims.Add(new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()));
            permClaims.Add(new Claim("valid", "1"));
            permClaims.Add(new Claim("userid", "1"));
            permClaims.Add(new Claim("name", "xpto"));
            //Create Security Token object by giving required parameters
            var token = new JwtSecurityToken(issuer, //Issure
            issuer, //Audience
            permClaims,
            expires: DateTime.Now.AddDays(1),
            signingCredentials: credentials);
            var jwt_token = new JwtSecurityTokenHandler().WriteToken(token);
            return new { data = jwt_token };
        }
    }
}


