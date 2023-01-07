using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using LayerBOL.Models;
using Npgsql;
using System.Data;
using System.Data.SqlClient;
using System.Numerics;
using System.Runtime.Intrinsics.Arm;
using LayerBLL.Utils;
using LayerBLL.Logics;
using Swashbuckle.AspNetCore.Annotations;
using StatusCodes = Microsoft.AspNetCore.Http.StatusCodes;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.AspNetCore.Authorization;

namespace Backend_IPCA_Gym.Controllers
{
    /// <summary>
    /// Controlador da entidade Funcionario
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class FuncionarioController : Controller
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador funcionario
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public FuncionarioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        /// <summary>
        /// Método http get para retornar os funcionários da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de funcionários em formato Json</returns>
        [HttpGet("history"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar os funcionários da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de funcionários em formato Json</returns>
        [HttpGet, Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> GetAllAtivo()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.GetAllAtivoLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar um funcionário através do seu id
        /// </summary>
        /// <param name="targetID">ID do funcionário que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e o funcionario em formato Json</returns>
        [HttpGet("{targetID}"), Authorize(Roles = "Admin, Cliente,Gerente, Funcionario")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo funcionário
        /// </summary>
        /// <param name="newFuncionario">Dados do novo funcionario a ser inserido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost, Authorize(Roles = "Admin, Gerente")]
        public async Task<IActionResult> Post([FromBody] Funcionario newFuncionario)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Verificar se Gerente é do mesmo gym que o novo funcionario

            Response response = await FuncionarioLogic.PostLogic(sqlDataSource, newFuncionario);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de um funcionário através do id e novos dados
        /// </summary>
        /// <param name="funcionario">funcionario com dados alterados</param>
        /// <param name="targetID">ID do funcionario pretendido para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> Patch([FromBody] Funcionario funcionario, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Verificar se Gerente pertence ao ginasio ao qual o funcionario em questao pertence tambem
            //Verificar se Funcionario, caso seja um patch em si proprio, só faz patch em si proprio

            Response response = await FuncionarioLogic.PatchLogic(sqlDataSource, funcionario, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um funcionário da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID da funcionario pretendido para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para efetuar o login de um funcionário
        /// </summary>
        /// <param name="conta">Model de login de Funcionario</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a token de sessão em formato json</returns>
        [Route("login")]
        [HttpPost]
        public async Task<IActionResult> Login([FromBody] LoginFuncionario conta)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.LoginLogic(sqlDataSource, conta, _configuration);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para recuperar a palavra passe de um funcionario
        /// </summary>
        /// <param name="codigo">Codigo do funcionario que se pretende recuperar a password</param>
        /// <param name="password">Nova password</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPatch("recoverpass")]
        public async Task<IActionResult> RecoverPassword(int codigo, string password)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Verificar se quem executa request é quem possui o codigo

            Response response = await FuncionarioLogic.RecoverPasswordLogic(codigo, password, sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para adicionar um novo cliente por parte do funcionario
        /// </summary>
        /// <param name="newCliente">Objeto que contém os dados do novo cliente</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPost("register/cliente"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> RegistClient([FromBody] Cliente newCliente)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Verificar se Gerente e Funcionario pertencem ao ginasio ao qual o cliente pertence

            Response response = await FuncionarioLogic.RegistClientLogic(sqlDataSource, newCliente);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para editar um cliente por parte do funcionario
        /// </summary>
        /// <param name="targetID">ID do funcionario a editar</param>
        /// <param name="cliente">Objeto que contém os dados atualizados do funcionário</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPatch("edit/cliente/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> EditCliente(int targetID, [FromBody] Cliente cliente)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Verificar se Gerente e Funcionario pertencem ao ginasio com ID = cliente.id_ginasio

            Response response = await FuncionarioLogic.EditClienteLogic(sqlDataSource, targetID, cliente);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para editar valor de stock de um produto
        /// </summary>
        /// <param name="targetID">ID do produto da Loja que se pretende alterar stock</param>
        /// <param name="quantidade">Novo valor de quantidade de stock na loja</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPatch("changestock/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> EditLojaStock(int targetID, int quantidade)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Verificar se Gerente e Funcionario pertencem ao ginasio com ID = targetID

            Response response = await FuncionarioLogic.EditLojaStockLogic(sqlDataSource, quantidade, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para editar um produto
        /// </summary>
        /// <param name="produto">Objeto que contém dados atualizados do produto da Loja</param>
        /// <param name="targetID">ID do produto pretendido a mudar</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPatch("edit/product/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionário")]
        public async Task<IActionResult> EditLoja(int targetID, [FromBody] Loja produto)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Verificar se Gerente e Funcionario pertencem ao ginasio com ID = targetID

            Response response = await FuncionarioLogic.EditLojaLogic(sqlDataSource, produto, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para editar a ocupação de um ginásio
        /// </summary>
        /// <param name="targetID">ID do ginásio a alterar a sua ocupação</param>
        /// <param name="lotacao">Valor da lotação atual do ginásio</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPatch("edit/ocupation/{targetID}"), Authorize(Roles = "Admin, Funcionario, Gerente")]
        public async Task<IActionResult> EditLotacaoGym(int targetID, int lotacao)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            
            //Verificar se Gerente e Funcionario pertencem ao ginasio com ID = targetID
            
            Response response = await FuncionarioLogic.EditLotacaoGymLogic(sqlDataSource, targetID, lotacao);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para ver as avaliacoes de um ginasio
        /// </summary>
        /// <param name="codigofuncionario">Codigo do funcionario que faz o request</param>
        /// <returns>Resposta do request que contém a sua mensagem, o seu código e a lista de avaliacoes em formato json</returns>
        [HttpGet("avaliacoes/{codigofuncionario}"), Authorize(Roles = "Admin, Funcionario, Gerente")]
        public async Task<IActionResult> GetAvaliacoesOnGym(int codigofuncionario)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            Response response = await FuncionarioLogic.GetAvaliacoesOnGymLogic(sqlDataSource, codigofuncionario);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para remover um funcionário com regras
        /// </summary>
        /// <param name="targetID">Id do funcionário que faz o request</param>
        /// <returns>Resposta do request que contém a sua mensagem, o seu código e a lista de avaliacoes em formato json</returns>
        [HttpGet("delete/{targetID}"), Authorize(Roles = "Admin, Gerente")]
        public async Task<IActionResult> DeleteFuncionario(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            Response response = await FuncionarioLogic.DeleteFuncionarioLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar um funcionário através da sua token de sessão
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e o funcionario em formato Json</returns>
        [HttpGet("getbytoken"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> GetFuncionarioByToken()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            string codigo = User.FindFirstValue(ClaimTypes.Email);

            Response response = await FuncionarioLogic.GetFuncionarioByTokenLogic(sqlDataSource, codigo);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar uma lista de funcionarios pertencentes a um ginasio
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de funcionarios em formato Json</returns>
        [HttpGet("allbygym/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> GetAllByGym(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            Response response = await FuncionarioLogic.GetAllByIDGinasioLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }
    }
}