﻿using Microsoft.AspNetCore.Http;
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
using Microsoft.AspNetCore.Authorization;

namespace Backend_IPCA_Gym.Controllers
{
    /// <summary>
    /// Controlador da entidade loja
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]

    public class LojaController : Controller
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador loja
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public LojaController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        #region DEFAULT REQUESTS

        /// <summary>
        /// Método http get para retornar os produtos da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de produtos em formato Json</returns>
        [HttpGet, Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await LojaLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar um produto através do seu id
        /// </summary>
        /// <param name="targetID">ID do produto que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e o produto em formato Json</returns>
        [HttpGet("{targetID}"), Authorize(Roles = "Admin, Cliente, Funcionario, Gerente")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await LojaLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo produto
        /// </summary>
        /// <param name="newLoja">Dados do novo produto a ser inserido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost, Authorize(Roles = "Admin, Gerente")]
        public async Task<IActionResult> Post([FromBody] Loja newLoja)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await LojaLogic.PostLogic(sqlDataSource, newLoja);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de um produto através do id e novos dados
        /// </summary>
        /// <param name="produto">produto com dados alterados</param>
        /// <param name="targetID">ID do produto pretendido para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}"), Authorize(Roles = "Admin, Gerente")]
        public async Task<IActionResult> Patch([FromBody] Loja produto, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await LojaLogic.PatchLogic(sqlDataSource, produto, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um produto da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID do produto pretendido para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await LojaLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método http get para retornar os produtos através do id do ginásio
        /// </summary>
        /// <param name="targetID">ID do ginásio que é pretendido que retorne os produtos</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e o produto em formato Json</returns>
        [HttpGet("Ginasio/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> GetAllByGinasioID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await LojaLogic.GetAllByGinasioIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion
    }
}
