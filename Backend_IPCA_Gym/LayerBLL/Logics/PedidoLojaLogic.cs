using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade PedidoLoja
    /// </summary>
    public class PedidoLojaLogic
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todas as associações pedido_loja
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<PedidoLoja> pedido_lojaList = await PedidoLojaService.GetAllService(sqlDataSource);

            if (pedido_lojaList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de produto_pedido obtida com sucesso";
                response.Data = new JsonResult(pedido_lojaList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter uma associação pedido_loja em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID1">ID do Pedido que é pretendido associar</param>
        /// <param name="targetID2">ID do produto da Loja que é pretendido associar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID1, int targetID2)
        {
            Response response = new Response();
            PedidoLoja pedido_loja = await PedidoLojaService.GetByIDService(sqlDataSource, targetID1, targetID2);

            if (pedido_loja != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Produto_pedido obtido com sucesso!";
                response.Data = new JsonResult(pedido_loja);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar uma associação pedido_loja
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newPedido_Loja">Objeto com os dados de PedidoLoja a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, PedidoLoja newPedido_Loja)
        {
            Response response = new Response();
            bool creationResult = await PedidoLojaService.PostService(sqlDataSource, newPedido_Loja);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto_pedido criado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar uma associação pedido_loja
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="pedido_loja">Objeto que contém os dados atualizados da associação pedido_loja</param>
        /// <param name="targetID1">ID do pedido de loja que é pretendido ser retornado</param>
        /// <param name="targetID2">ID do produto de loja que é pretendido ser retornado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, PedidoLoja pedido_loja, int targetID1, int targetID2)
        {
            Response response = new Response();
            bool updateResult = await PedidoLojaService.PatchService(sqlDataSource, pedido_loja, targetID1, targetID2);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto_pedido alterado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar uma associação pedido_loja
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID1">ID do pedido de loja que é pretendido ser retornado</param>
        /// <param name="targetID2">ID do produto de loja que é pretendido ser retornado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID1, int targetID2)
        {
            Response response = new Response();
            bool deleteResult = await PedidoLojaService.DeleteService(sqlDataSource, targetID1, targetID2);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto_pedido apagado com sucesso!");
            }

            return response;
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os produtos de um pedido
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do pedido que é pretendido retornar os produtos</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllByPedidoIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            List<PedidoLoja> pedidoLojaList = await PedidoLojaService.GetAllByPedidoIDService(sqlDataSource, targetID);

            if (pedidoLojaList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de pedidos de loja obtida com sucesso";
                response.Data = new JsonResult(pedidoLojaList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar todas as associações pedido_loja de um id de um pedido
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID1">ID do pedido de loja que é pretendido que se remova as associações</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeletePedidoLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await PedidoLojaService.DeletePedidoService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto_pedido apagado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar uma associação pedido_loja com verificações de stock e manipulação de stock
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newPedido_Loja">Objeto com os dados de PedidoLoja a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostPedidoCheckedLogic(string sqlDataSource, PedidoLoja newPedido_Loja)
        {
            Response response = new Response();
            bool creationResult = await PedidoLojaService.PostPedidoCheckedService(sqlDataSource, newPedido_Loja);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto_pedido criado com sucesso!");
            }

            return response;
        }

        #endregion
    }
}
