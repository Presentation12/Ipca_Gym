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
    public class LojaLogic
    {
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Loja> lojaList = await LojaService.GetAllService(sqlDataSource);

            if (lojaList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de produtos obtida com sucesso";
                response.Data = new JsonResult(lojaList);
            }

            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Loja loja = await LojaService.GetByIDService(sqlDataSource, targetID);

            if (loja != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Produto obtida com sucesso!";
                response.Data = new JsonResult(loja);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, Loja newLoja)
        {
            Response response = new Response();
            bool creationResult = await LojaService.PostService(sqlDataSource, newLoja);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto criado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, Loja loja, int targetID)
        {
            Response response = new Response();
            bool updateResult = await LojaService.PatchService(sqlDataSource, loja, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto alterado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await LojaService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto apagado com sucesso!");
            }

            return response;
        }
    }
}
