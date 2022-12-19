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

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Pedido where id_pedido = @id_pedido";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_pedido", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Pedido targetPedido = new Pedido();
                        targetPedido.id_pedido = reader.GetInt32(0);
                        targetPedido.id_cliente = reader.GetInt32(1);
                        targetPedido.estado = reader.GetString(2);
                        targetPedido.data_pedido = reader.GetDateTime(3);
                        
                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetPedido);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Pedido newPedido)
        {
            string query = @"
                            insert into dbo.Pedido (id_cliente, data_pedido, estado)
                            values (@id_cliente, @data_pedido, @estado)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_cliente", newPedido.id_cliente);
                    myCommand.Parameters.AddWithValue("data_pedido", Convert.ToDateTime(newPedido.data_pedido));
                    myCommand.Parameters.AddWithValue("estado", newPedido.estado);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Pedido adicionado com sucesso");
        }

        [HttpPatch]
        public IActionResult Update(Pedido pedido)
        {
            string query = @"
                            update dbo.Pedido 
                            set id_cliente = @id_cliente, 
                            data_pedido = @data_pedido,
                            estado = @estado
                            where id_pedido = @id_pedido";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_pedido", pedido.id_pedido);
                    myCommand.Parameters.AddWithValue("id_cliente", pedido.id_cliente);
                    myCommand.Parameters.AddWithValue("data_pedido", Convert.ToDateTime(pedido.data_pedido));
                    myCommand.Parameters.AddWithValue("estado", pedido.estado);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Pedido atualizado com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Pedido 
                            where id_pedido = @id_pedido";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_pedido", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Pedido apagado com sucesso");
        }
    }
}
