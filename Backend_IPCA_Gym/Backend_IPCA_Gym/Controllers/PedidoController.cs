using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PedidoController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public PedidoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Pedido";
            List<Pedido> pedidos = new List<Pedido>();

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        Pedido pedido = new Pedido();

                        pedido.id_pedido = Convert.ToInt32(dataReader["id_pedido"]);
                        pedido.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                        pedido.data_pedido = Convert.ToDateTime(dataReader["data_pedido"]);
                        pedido.estado = dataReader["estado"].ToString();

                        pedidos.Add(pedido);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(pedidos);
        }
    }
}
