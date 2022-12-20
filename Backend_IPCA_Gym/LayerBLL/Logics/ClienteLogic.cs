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
    public class ClienteLogic
    {
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Cliente> clienteList = await ClienteService.GetAllService(sqlDataSource);

            if (clienteList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de clientes obtidos com sucesso";
                response.Data = new JsonResult(clienteList);
            }

            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Cliente cliente = await ClienteService.GetByIDService(sqlDataSource, targetID);

            if (cliente != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Cliente obtido com sucesso";
                response.Data = new JsonResult(cliente);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, Cliente newCliente)
        {
            Response response = new Response();
            bool creationResult = await ClienteService.PostService(sqlDataSource, newCliente);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Cliente adicionado com sucesso");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, Cliente cliente, int targetID)
        {
            Response response = new Response();
            bool updateResult = await ClienteService.PatchService(sqlDataSource, cliente, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Cliente alterado com sucesso");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await ClienteService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Cliente removido com sucesso");
            }

            return response;
        }
    }
}
