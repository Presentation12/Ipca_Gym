using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System.Data.SqlClient;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PedidoLojaController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public PedidoLojaController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Pedido_Loja";
            List<PedidoLoja> pedidosloja = new List<PedidoLoja>();

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
                        PedidoLoja pedidoloja = new PedidoLoja();

                        pedidoloja.id_pedido = Convert.ToInt32(dataReader["id_pedido"]);
                        pedidoloja.id_produto = Convert.ToInt32(dataReader["id_produto"]);
                        pedidoloja.quantidade = Convert.ToInt32(dataReader["quantidade"]);

                        pedidosloja.Add(pedidoloja);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(pedidosloja);
        }

        [HttpGet("search")]
        public IActionResult GetByID(int targetID1, int targetID2)
        {
            string query = @"
                            select * from dbo.Pedido_Loja where id_pedido = @id_pedido and id_produto = @id_produto";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_pedido", targetID1);
                    myCommand.Parameters.AddWithValue("id_produto", targetID2);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        PedidoLoja targetPedidoLoja = new PedidoLoja();
                        targetPedidoLoja.id_pedido = reader.GetInt32(0);
                        targetPedidoLoja.id_produto = reader.GetInt32(1);
                        targetPedidoLoja.quantidade = reader.GetInt32(2);

                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetPedidoLoja);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(PedidoLoja newPedidoLoja)
        {
            string query = @"
                            insert into dbo.Pedido_Loja (id_pedido, id_produto, quantidade)
                            values (@id_pedido, @id_produto, @quantidade)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_pedido", newPedidoLoja.id_pedido);
                    myCommand.Parameters.AddWithValue("id_produto", newPedidoLoja.id_produto);
                    myCommand.Parameters.AddWithValue("quantidade", newPedidoLoja.quantidade);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Pedido Loja adicionado com sucesso");
        }

        [HttpPatch]
        public IActionResult Update(PedidoLoja pedidoLoja)
        {
            string query = @"
                            update dbo.Pedido_Loja 
                            set quantidade = @quantidade
                            where id_pedido = @id_pedido and id_produto = @id_produto";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_pedido", pedidoLoja.id_pedido);
                    myCommand.Parameters.AddWithValue("id_produto", pedidoLoja.id_produto);
                    myCommand.Parameters.AddWithValue("quantidade", pedidoLoja.quantidade);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Pedido Loja atualizado com sucesso");
        }

        [HttpDelete("search")]
        public IActionResult Delete(int targetID1, int targetID2)
        {
            string query = @"
                            delete from dbo.Pedido_Loja 
                            where id_pedido = @id_pedido and id_produto = @id_produto";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_pedido", targetID1);
                    myCommand.Parameters.AddWithValue("id_produto", targetID2);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Pedido Loja apagado com sucesso");
        }
    }
}
