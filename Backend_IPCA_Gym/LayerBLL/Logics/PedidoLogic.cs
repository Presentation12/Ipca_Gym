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
    /// Classe que contém a lógica do response da entidade Pedido
    /// </summary>
    public class PedidoLogic
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os pedidos
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Pedido> pedidoList = await PedidoService.GetAllService(sqlDataSource);

            if (pedidoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de pedidos obtida com sucesso";
                response.Data = new JsonResult(pedidoList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter um pedido em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Pedido que é pretendido retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Pedido pedido = await PedidoService.GetByIDService(sqlDataSource, targetID);

            if (pedido != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Pedido obtido com sucesso!";
                response.Data = new JsonResult(pedido);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar um pedido
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newPedido">Objeto com os dados do Pedido a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Pedido newPedido)
        {
            Response response = new Response();
            bool creationResult = await PedidoService.PostService(sqlDataSource, newPedido);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Pedido criado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar um pedido
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="pedido">Objeto que contém os dados atualizados do Pedido</param>
        /// <param name="targetID">ID do Pedido que é pretendido atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Pedido pedido, int targetID)
        {
            Response response = new Response();
            bool updateResult = await PedidoService.PatchService(sqlDataSource, pedido, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Pedido alterado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar um pedido
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Pedido que é pretendido eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await PedidoService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Pedido apagado com sucesso!");
            }

            return response;
        }

        #endregion

        #region BACKLOG REQUESTS    

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os pedidos com seus produtos de um cliente (join)
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do Cliente que é pretendido retornar os dados</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllConnectionClientLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            List<Object> pedidoList = await PedidoService.GetAllConnectionClientService(sqlDataSource, targetID);

            if (pedidoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de pedidos obtida com sucesso!";
                response.Data = new JsonResult(pedidoList);
            }

            return response;
        }

        #endregion
    }
}
