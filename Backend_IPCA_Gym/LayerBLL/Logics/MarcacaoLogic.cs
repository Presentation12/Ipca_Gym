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
    public class MarcacaoLogic
    {
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Marcacao> marcacaoList = await MarcacaoService.GetAllService(sqlDataSource);

            if (marcacaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de marcacoes obtida com sucesso";
                response.Data = new JsonResult(marcacaoList);
            }

            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Marcacao marcacao = await MarcacaoService.GetByIDService(sqlDataSource, targetID);

            if (marcacao != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Produto obtida com sucesso!";
                response.Data = new JsonResult(marcacao);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, Marcacao newMarcacao)
        {
            Response response = new Response();
            bool creationResult = await MarcacaoService.PostService(sqlDataSource, newMarcacao);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Marcacao criada com sucesso!");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, Marcacao marcacao, int targetID)
        {
            Response response = new Response();
            bool updateResult = await MarcacaoService.PatchService(sqlDataSource, marcacao, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Marcacao alterada com sucesso!");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await MarcacaoService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Marcacao apagada com sucesso!");
            }

            return response;
        }
    }
}
