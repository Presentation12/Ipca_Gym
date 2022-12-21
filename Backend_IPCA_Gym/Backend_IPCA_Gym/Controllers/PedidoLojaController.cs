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

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PedidoLojaController : Controller
    {
        private readonly IConfiguration _configuration;
        public PedidoLojaController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        [HttpGet("{targetID}")]
        public async Task<IActionResult> GetByID(int targetID1, int targetID2)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.GetByIDLogic(sqlDataSource, targetID1, targetID2);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody] PedidoLoja newPedidoLoja)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.PostLogic(sqlDataSource, newPedidoLoja);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        [HttpPatch("{targetID}")]
        public async Task<IActionResult> Patch([FromBody] PedidoLoja pedidoLoja, int targetID1, int targetID2)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.PatchLogic(sqlDataSource, pedidoLoja, targetID1, targetID2);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        [HttpDelete("{targetID}")]
        public async Task<IActionResult> Delete(int targetID1, int targetID2)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.DeleteLogic(sqlDataSource, targetID1, targetID2);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }
    }
}
